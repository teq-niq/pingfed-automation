
plugins {
	  id ("org.siouan.frontend-jdk11") version "6.0.0"

}



frontend{
    nodeVersion.set("18.13.0")
    // See 'scripts' section in your 'package.json file'
  nodeDistributionUrlRoot.set("https://nodejs.org/dist/")
    assembleScript.set("run build")

}
