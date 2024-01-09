`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date: 01/08/2024 08:05:51 PM
// Design Name: 
// Module Name: fir_compiler_top
// Project Name: 
// Target Devices: 
// Tool Versions: 
// Description: 
// 
// Dependencies: 
// 
// Revision:
// Revision 0.01 - File Created
// Additional Comments:
// 
//////////////////////////////////////////////////////////////////////////////////


module fir_compiler_top(
    input           aclk,
    input           s_axis_data_tvalid,
    output          s_axis_data_tready,
    input[15:0]     s_axis_data_tdata,
    output[39:0]    m_axis_data_tdata,
    output          m_axis_data_tvalid
    );
    
//----------- Begin Cut here for INSTANTIATION Template ---// INST_TAG
fir_compiler_0 fir_try (
  .aclk(aclk),                              // input wire aclk
  .s_axis_data_tvalid(s_axis_data_tvalid),  // input wire s_axis_data_tvalid
  .s_axis_data_tready(s_axis_data_tready),  // output wire s_axis_data_tready
  .s_axis_data_tdata(s_axis_data_tdata),    // input wire [15 : 0] s_axis_data_tdata
  .m_axis_data_tvalid(m_axis_data_tvalid),  // output wire m_axis_data_tvalid
  .m_axis_data_tdata(m_axis_data_tdata)    // output wire [39 : 0] m_axis_data_tdata
);
// INST_TAG_END ------ End INSTANTIATION Template ---------

// You must compile the wrapper file fir_compiler_0.v when simulating
// the core, fir_compiler_0. When compiling the wrapper file, be sure to
// reference the Verilog simulation library.
endmodule
