name := "learning-zio"
version in ThisBuild := "0.1.0"
organization in ThisBuild := "info.galudisu"
scalaVersion in ThisBuild := "2.13.3"

lazy val root = project
  .in(file("."))
  .settings(settings)
  .aggregate(common, chap01)

lazy val common = project
  .in(file("common"))
  .settings(
    name := "common",
    settings,
    libraryDependencies ++= commonDependencies
  )
lazy val chap01 = project
  .in(file("chap01"))
  .settings(
    name := "chap01",
    settings,
    libraryDependencies ++= commonDependencies
  )
  .dependsOn(common)

lazy val settings = Seq(
  javacOptions := Seq(
    "-J-XX:+UnlockExperimentalVMOptions",
    "-J-XX:+UseCGroupMemoryLimitForHeap",
    "-J-XX:MaxRAMFraction=1",
    "-J-XshowSettings:vm"
  ),
  scalacOptions in Compile in console := Seq(
    "-Ypartial-unification",
    "-language:higherKinds",
    "-language:existentials",
    "-Yno-adapted-args",
    "-Xsource:2.13",
    "-Yrepl-class-based",
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-explaintypes",
    "-Yrangepos",
    "-feature",
    "-Xfuture",
    "-unchecked",
    "-Xlint:_,-type-parameter-shadow",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard",
    "-opt-warnings",
    "-Ywarn-extra-implicit",
    "-Ywarn-unused:_,imports",
    "-Ywarn-unused:imports",
    "-opt:l:inline",
    "-opt-inline-from:<source>",
    "-Ypartial-unification",
    "-Yno-adapted-args",
    "-Ywarn-inaccessible",
    "-Ywarn-infer-any",
    "-Ywarn-nullary-override",
    "-Ywarn-nullary-unit"
  ),
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  )
)

lazy val dependencies = new {
  val zioV           = "1.0.3"
  val logbackV       = "1.2.3"
  val scalaUriV      = "3.0.0"
  val zioCore        = "dev.zio" %% "zio" % zioV
  val zioStreams     = "dev.zio" %% "zio-streams" % zioV
  val lemonlabs      = "io.lemonlabs" %% "scala-uri" % scalaUriV
  val logbackClassic = "ch.qos.logback" % "logback-classic" % logbackV
}

lazy val commonDependencies = Seq(
  dependencies.zioCore,
  dependencies.zioStreams,
  dependencies.lemonlabs,
  dependencies.logbackClassic
)
