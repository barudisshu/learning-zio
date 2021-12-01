ThisBuild / name := "learning-zio"
ThisBuild / version := "0.1.0"
ThisBuild / organization := "info.galudisu"
ThisBuild / scalaVersion := "2.13.6"
ThisBuild / homepage := Some(url("https://github.com/barudisshu/learning-zio"))
ThisBuild / licenses := List("MIT" -> url("https://opensource.org/licenses/MIT"))
ThisBuild / developers := List(
  Developer(
    "barudisshu",
    "Galudisu",
    "galudisu@gmail.com",
    url("https://galudisu.info")
  )
)

lazy val root = project
  .in(file("."))
  .settings(name := "learning-zio", settings)
  .aggregate(common, chap00, chap01, chap02, chap03, chap04, chap05)

lazy val common = project
  .in(file("common"))
  .settings(name := "common", settings, libraryDependencies ++= commonDependencies)
lazy val chap00 = project
  .in(file("chap00"))
  .settings(name := "chap00", settings, libraryDependencies ++= commonDependencies)
  .dependsOn(common)
lazy val chap01 = project
  .in(file("chap01"))
  .settings(name := "chap01", settings, libraryDependencies ++= commonDependencies)
  .dependsOn(common)
lazy val chap02 = project
  .in(file("chap02"))
  .settings(name := "chap02",
            settings,
            libraryDependencies ++= commonDependencies ++ akkaDependencies ++ doobieDependencies ++ monadDependencies)
  .dependsOn(common)
lazy val chap03 = project
  .in(file("chap03"))
  .settings(name := "chap03",
            settings,
            libraryDependencies ++= commonDependencies ++ akkaDependencies ++ monadDependencies)
  .dependsOn(common)
lazy val chap04 = project
  .in(file("chap04"))
  .settings(name := "chap04",
            settings,
            libraryDependencies ++= commonDependencies ++ nettyDependencies ++ bouncycastleDependencies)
  .dependsOn(common)
lazy val chap05 = project
  .in(file("chap05"))
  .settings(name := "chap05", settings, libraryDependencies ++= commonDependencies)
  .dependsOn(common)

lazy val settings = Seq(
  javacOptions := Seq(
    "-J-XX:+UnlockExperimentalVMOptions",
    "-J-XX:+UseCGroupMemoryLimitForHeap",
    "-J-XX:MaxRAMFraction=1",
    "-J-XshowSettings:vm"
  ),
  Compile / console / scalacOptions := Seq(
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
  val zioV            = "1.0.12"
  val zioInteropCatsV = "3.2.9.0"

  val scalaUriV      = "3.6.0"
  val akkaV          = "2.6.17"
  val akkaHttpV      = "10.2.7"
  val circeV         = "0.14.1"
  val akkaHttpCirceV = "1.38.2"
  val nettyV         = "4.1.70.Final"
  val zioActorV      = "0.0.9"
  val doobieV        = "1.0.0-RC1"
  val bouncycastleV  = "1.69"
  val log4jV         = "2.14.1"

  // Dev ZIO
  val zioCore        = "dev.zio" %% "zio"              % zioV
  val zioStreams     = "dev.zio" %% "zio-streams"      % zioV
  val zioActor       = "dev.zio" %% "zio-actors"       % zioActorV
  val zioInteropCats = "dev.zio" %% "zio-interop-cats" % zioInteropCatsV

  // Akka
  val akkaCore      = "com.typesafe.akka" %% "akka-actor-typed"         % akkaV
  val akkaSlf4j     = "com.typesafe.akka" %% "akka-slf4j"               % akkaV
  val AkkaHttp      = "com.typesafe.akka" %% "akka-http"                % akkaHttpV
  val akkaTestKit   = "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaV
  val akkaStream    = "com.typesafe.akka" %% "akka-stream"              % akkaV
  val circeCore     = "io.circe"          %% "circe-generic"            % circeV
  val akkaHttpCirce = "de.heikoseeberger" %% "akka-http-circe"          % akkaHttpCirceV

  // Netty
  val nettyTransport = "io.netty" % "netty-transport"              % nettyV
  val nettyEpoll     = "io.netty" % "netty-transport-native-epoll" % nettyV classifier "linux-x86_64"
  val nettyHttp      = "io.netty" % "netty-codec-http"             % nettyV
  val nettyHttp2     = "io.netty" % "netty-codec-http2"            % nettyV

  // Doobie
  val doobieCore     = "org.tpolecat" %% "doobie-core"     % doobieV
  val doobiePostgres = "org.tpolecat" %% "doobie-postgres" % doobieV

  // bouncy castle JSSE provider
  val bouncycastlePkix = "org.bouncycastle" % "bcpkix-jdk15on" % bouncycastleV
  val bouncycastleTls  = "org.bouncycastle" % "bctls-jdk15on"  % bouncycastleV

  val lemonlabs = "io.lemonlabs" %% "scala-uri" % scalaUriV

  // Log4j2
  val log4j2Core = "org.apache.logging.log4j" % "log4j-core"       % log4jV
  val log4j2Api  = "org.apache.logging.log4j" % "log4j-api"        % log4jV
  val log4j2Impl = "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4jV
}

lazy val commonDependencies = Seq(
  dependencies.zioCore,
  dependencies.zioStreams,
  dependencies.zioActor,
  dependencies.lemonlabs,
  dependencies.log4j2Core,
  dependencies.log4j2Api,
  dependencies.log4j2Impl
)

lazy val akkaDependencies = Seq(
  dependencies.akkaCore,
  dependencies.akkaTestKit,
  dependencies.akkaSlf4j,
  dependencies.AkkaHttp,
  dependencies.akkaStream,
  dependencies.circeCore,
  dependencies.akkaHttpCirce
)

lazy val nettyDependencies = Seq(
  dependencies.nettyTransport,
  dependencies.nettyEpoll,
  dependencies.nettyHttp,
  dependencies.nettyHttp2
)

lazy val doobieDependencies = Seq(
  dependencies.doobieCore,
  dependencies.doobiePostgres
)

lazy val bouncycastleDependencies = Seq(
  dependencies.bouncycastlePkix,
  dependencies.bouncycastleTls
)

lazy val monadDependencies = Seq(
  dependencies.zioInteropCats
)
