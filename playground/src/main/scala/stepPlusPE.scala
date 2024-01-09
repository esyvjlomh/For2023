package main.scala

import chisel3._
import chisel3.util.log2Up

class stepPlusPE(step: Int,ifmsize: Int,n: Int) extends Module {
  val s = n * n
  val ifm = ifmsize * ifmsize
//  val u = ifmsize
//  val data_count = s / 4 + 1
  val odd = (s%2).U === 0.U

  val io = IO(new Bundle {
    val addr = Input(UInt(log2Up(ifm).W))
    val dataIn = Input(SInt(8.W))
    val en = Input(Bool())
    val we = Input(Bool())
    val fetch = Input(Bool())
    val weightIn = Input(UInt((8 * s).W))
    val sumOut = Output(SInt(22.W))
  })

  val stepRAM = Module(new Step(step,ifmsize,n))
  val PE = Module(new PE_Parameterized(n))

  stepRAM.io.addr := io.addr
  stepRAM.io.dataIn := io.dataIn
  stepRAM.io.en := io.en
  stepRAM.io.we := io.we
  stepRAM.io.fetch := io.fetch
  PE.io.weightIn := io.weightIn
  io.sumOut := PE.io.sumOut

  stepRAM.io.dataOut <> PE.io.dataIn
}

