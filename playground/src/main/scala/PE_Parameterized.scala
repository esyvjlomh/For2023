/*
 * This module can generate parameterized PE module.
 * PE kernel size is paramitized.
 * n: Defines the kernel size. For example, if n==4, the kernel_size==4x4.
 */
package CNN

import chisel3._
//import chisel3.experimental._(Prepared for Fixedpoint)

class PE_Parameterized(n:Int) extends Module {
  val s = n * n
  val odd = (s%2).U === 0.U
  //Some auxiliary parameters (scala type).

  def odd_top(k:Int)=
    if(k%2==0) k/2
    else k/2+1
  //This small function returns the number of outputs which depends on the parity.

  val io = IO(new Bundle {
    val dataIn = Input(UInt((8 * s).W))
    val weightIn = Input(UInt((8 * s).W))
    val sumOut = Output(SInt(22.W))
  })
  //IO definition. Now the dataIn&&weightIn should be UInt because it is combined.

  val dataInVec = WireInit(VecInit(Seq.fill(s)(0.S(8.W))))
  val weightInVec = WireInit(VecInit(Seq.fill(s)(0.S(8.W))))
  val product0x = WireInit(VecInit(Seq.fill(s)(0.S(16.W))))
  val productReg = RegInit(VecInit(Seq.fill(s)(0.S(16.W))))
  //Use these Vecs to combine datas.The Vecs you want to use must be initialized.

  for (i <- 0 until s) {
    dataInVec(i) := io.dataIn(8 * i + 7, 8 * i).asSInt
    weightInVec(i) := io.weightIn(8 * i + 7, 8 * i).asSInt
  }
  //Use this for loop to cut data, the .asSInt() function converts the bits to signed data.

  for (i <- 0 until s) {
    productReg(i) := dataInVec(i) * weightInVec(i)
  }
  //Multiply function.Give the wire value to a bundle of regs.

  val addIn0Vec = WireInit(VecInit(Seq.fill(s)(0.S(17.W))))
  for (i <- 0 until s) {
    addIn0Vec(i) := productReg(i)
  }
  //Use the regs to add!

  //Use 6-level-max hierarchy to speed up the adding process.
  val pipeline1 = Module(new pipeline(s,1))
  for (i <- 0 until s) {
    pipeline1.io.dataIn(i) := addIn0Vec(i)
  }
  //level 1
  if(s==1) io.sumOut := addIn0Vec(0)
  else {
    val pipeline2 = Module(new pipeline(odd_top(s), 2))
    pipeline1.io.dataOut <> pipeline2.io.dataIn
    //level 2
    if (odd_top(odd_top(s)) == 1)
      io.sumOut := pipeline2.io.dataOut(0)
    else {
      val pipeline3 = Module(new pipeline(odd_top(odd_top(s)), 3))
      pipeline2.io.dataOut <> pipeline3.io.dataIn
      //level3
      if (odd_top(odd_top(odd_top(s))) == 1)
        io.sumOut := pipeline3.io.dataOut(0)
      else {
        val pipeline4 = Module(new pipeline(odd_top(odd_top(odd_top(s))), 4))
        pipeline3.io.dataOut <> pipeline4.io.dataIn
        //level 4
        if (odd_top(odd_top(odd_top(odd_top(s)))) == 1)
          io.sumOut := pipeline4.io.dataOut(0)
        else {
          val pipeline5 = Module(new pipeline(odd_top(odd_top(odd_top(odd_top(s)))), 5))
          pipeline4.io.dataOut <> pipeline5.io.dataIn
          //level 5
          if (odd_top(odd_top(odd_top(odd_top(odd_top(s))))) == 1)
            io.sumOut := pipeline5.io.dataOut(0)
          else {
            val pipeline6 = Module(new pipeline(odd_top(odd_top(odd_top(odd_top(odd_top(s))))), 6))
            pipeline5.io.dataOut <> pipeline6.io.dataIn
            //level 6 (max)
            io.sumOut := pipeline6.io.dataOut(0)
          }
        }
      }
    }
  }
}



