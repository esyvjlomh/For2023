package main.scala

import chisel3._
import chisel3.util._

class fir_compiler_top extends BlackBox with HasBlackBoxPath {
  val io = IO(new Bundle() {
    val aclk = Input(Bool())
    val s_axis_data_tvalid = Input(Bool())
    val s_axis_data_tready = Output(Bool())
    val s_axis_data_tdata = Input(SInt(16.W))
    val m_axis_data_tdata = Output(SInt(40.W))
    val m_axis_data_tvalid = Output(Bool())
  })
  addPath("./playground/src/resources/fir_compiler_top.v")
}
