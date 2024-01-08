package CNN

import chisel3._
import chisel3.util.{Cat, log2Up}

class Step(step:Int,ifmsize:Int,n:Int) extends Module {
  val s = n * n
  val ifm = ifmsize * ifmsize
  val u = ifmsize
  val data_count = s / 4 + 1
  val conv_count = ((ifm - (n-1) * (ifmsize+1)) / step) + 2

  val io = IO(new Bundle {
    val addr = Input(UInt(log2Up(ifm).W))
    val dataIn = Input(SInt(8.W))
    val en = Input(Bool())
    val we = Input(Bool())
    val fetch = Input(Bool())
    val dataOut = Output(UInt((8 * s).W))
    //val RAM = Output(SInt(8.W))
    //val countout = Output(UInt(8.W))
    //val convOut = Output(UInt(log2Up(ifm).W))
  })
  // I/O definition. addr/dataIn/en/we controls local RAM, en also controls dataOut.

  val localRAM = SyncReadMem(ifm, SInt(8.W))
  val RAMOut = WireInit(0.S(8.W))
  val ready = Wire(Bool())
  val shiftReg = RegInit(0.U((8 * s).W))
  val countReg = RegInit(0.U(8.W))
  val addrReg = RegInit(0.U(log2Up(ifm).W))
  val numReg = RegInit(0.U(8.W))
  val convReg = RegInit(0.U(log2Up(ifm).W))
  val outReg = RegInit(0.U((8 * s).W))

  io.dataOut := outReg
  //io.RAM := RAMOut
  //io.countout := countReg
  addrReg := io.addr
  //io.convOut := convReg
  ready := false.B

  //Initialize registers and output.

  when(io.en) {
    when(io.we) {
      localRAM.write(addrReg, io.dataIn)
      RAMOut := DontCare
    }.elsewhen(io.fetch) {
      RAMOut := localRAM.read(convReg)
    }.otherwise {
      RAMOut := localRAM.read(addrReg)
    }
  }.otherwise {
    RAMOut := DontCare
  }
  //General localRAM read and write.

  if (n == 1) {
    when(io.fetch){
      when(convReg < ifm.U) {
        outReg := RAMOut.asUInt
        convReg := convReg + step.U
        ready := true.B

      }.otherwise{
        outReg := RAMOut.asUInt
        convReg := ifm.U
      }
    }

  }
  else {
    when(io.fetch) {
      when(countReg === s.U){
        shiftReg := 0.U
        outReg := 0.U

      }.elsewhen(numReg === conv_count.U && countReg === 0.U) {
        shiftReg := Cat(shiftReg(8 * s - 9, 0), RAMOut.asUInt)
        convReg := ifm.U
        countReg := s.U

      }.elsewhen((countReg + 1.U) % n.U === 0.U && (countReg + 1.U) =/= s.U) {
        shiftReg := Cat(shiftReg(8 * s - 9, 0), RAMOut.asUInt)
        convReg := convReg + (ifmsize - n + 1).U
        countReg := countReg + 1.U

      }.elsewhen(countReg === (s - 1).U) {
        shiftReg := Cat(shiftReg(8 * s - 9, 0), RAMOut.asUInt)
        numReg := numReg + 1.U
        convReg := (numReg + 1.U) * step.U
        countReg := 0.U

      }.otherwise {
        shiftReg := Cat(shiftReg(8 * s - 9, 0), RAMOut.asUInt)
        countReg := countReg + 1.U
        convReg := convReg + 1.U
      }
    }

    when(countReg === 1.U) {
      ready := true.B
    }.otherwise {
      ready := false.B
    }

    when(ready) {
      outReg := shiftReg
    }
  }
}


