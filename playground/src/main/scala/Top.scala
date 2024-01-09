package main.scala

import chisel3._
import chisel3.util._

class TopIO extends Bundle{
  val aclk = Input(Bool())
  val s_axis_data_tvalid = Input(Bool())
  val s_axis_data_tready = Output(Bool())
  val s_axis_data_tdata = Input(SInt(16.W))
  val m_axis_data_tdata = Output(SInt(40.W))
  val m_axis_data_tvalid = Output(Bool())
}
class Top extends Module {
  val io = IO(new TopIO)
  val blackModule = Module(new fir_compiler_top)
  blackModule.io <> io
}
