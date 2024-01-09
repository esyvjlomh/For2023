import CNN.stepPlusPE
import _root_.circt.stage._
import main.scala.Top

//object Elaborate extends App {
//  def top = new stepPlusPE(3,8,3)
//  val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
//  (new ChiselStage).execute(args, generator :+ CIRCTTargetAnnotation(CIRCTTarget.Verilog))
//}

object Elaborate extends App {
  def top = new Top
  val generator = Seq(chisel3.stage.ChiselGeneratorAnnotation(() => top))
  (new ChiselStage).execute(args, generator :+ CIRCTTargetAnnotation(CIRCTTarget.Verilog))
}