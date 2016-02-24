lazy val root = (project in file("."))
.enablePlugins(PlayScala, sbtdocker.DockerPlugin)
.settings(
  scalaVersion := "2.11.7",
  scalacOptions := Seq("-language:_", "-deprecation", "-unchecked", "-feature", "-Xlint"),
  javaOptions := Seq("-Dplay.evolutions.db.default.autoApply=true"),
  transitiveClassifiers in Global := Seq(Artifact.SourceClassifier),
  sources in (Compile, doc) := Nil,
  publishArtifact in (Compile, packageDoc) := false,
  parallelExecution in Test := false
).settings(
  resolvers += Resolver.file(
    "local-ivy-repos", file(Path.userHome + "/.ivy2/local")
  )(Resolver.ivyStylePatterns),
  libraryDependencies ++= Seq(
    jdbc,
    evolutions,
    "org.skinny-framework" %% "skinny-orm" % "2.0.5",
    "org.scalikejdbc" %% "scalikejdbc-play-dbapi-adapter" % "2.4.3",
    "com.nulab-inc" %% "play2-oauth2-provider" % "0.16.1"
  )
)

dockerfile in docker := {
  val appDir = stage.value
  val targetDir = "/app"

  new Dockerfile {
    from("java")
    entryPoint(s"$targetDir/bin/${executableScriptName.value}")
    copy(appDir, targetDir)
  }
}

imageNames in docker := Seq(
  ImageName("letusfly85/play-sample-auth:latest")
)

routesGenerator := InjectedRoutesGenerator