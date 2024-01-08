/*
 * This module provides paramitized pipeline of adding function.
 * n: The number of inputs in this pipeline.
 * level: Which level does this pipeline stands.(Different level needs different expanding of the signal bit)
 */
package CNN
import chisel3._

class pipeline(n:Int,level:Int) extends Module {
  def odd(k:Int)=
    if(k%2==0) k/2
    else k/2+1
  // This small function returns the number of outputs which depends on the parity.

  val io = IO(new Bundle {
    val dataIn=Input(Vec(n,SInt((16+level).W)))
    val dataOut=Output(Vec(odd(n),SInt((17+level).W)))
  })
  //Definition of IO (two Vecs).
  val addInVec=WireInit(VecInit(Seq.fill(n+1)(0.S((16+level).W))))
  val addOutReg=RegInit(VecInit(Seq.fill(odd(n))(0.S((17+level).W))))

  for (i <- 0 until n){
    addInVec(i) := io.dataIn(i)
  }

  for (i <- 0 until odd(n)){
    addOutReg(i) := addInVec(2*i)+addInVec(2*i+1)
  }
  //Adding function.

  for (i <- 0 until odd(n)) {
    io.dataOut(i) := addOutReg(i)
  }
  //io.dataOut stores the output Vec.

}

