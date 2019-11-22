import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2019.2"

project {

    vcsRoot(BuildtwoRoot)
    vcsRoot(BuildoneRoot)

    buildType(BuildThreeSameVcs)
    buildType(BuildTwoSameVcs)
    buildType(BuildOne)

    template(Template_1)
}

object BuildOne : BuildType({
    name = "build one"

    vcs {
        root(BuildoneRoot)
    }

    steps {
        script {
            scriptContent = "echo build one"
        }
    }

    triggers {
        vcs {
            enabled = false
        }
    }

    dependencies {
        snapshot(BuildTwoSameVcs) {
        }
    }
})

object BuildThreeSameVcs : BuildType({
    name = "build three same VCS"
    
params {
            param("params", "2")
        }
    
    vcs {
        root(BuildoneRoot)
    }

    steps {
        script {
            scriptContent = "echo build one"
        }
    }

    triggers {
        vcs {
            enabled = false
        }
    }
})

object BuildTwoSameVcs : BuildType({
    name = "build two same VCS"

    vcs {
        root(BuildoneRoot)
        root(BuildtwoRoot)
    }

    steps {
        script {
            scriptContent = "echo build one"
        }
    }

    triggers {
        vcs {
            enabled = false
        }
    }

    dependencies {
        snapshot(BuildThreeSameVcs) {
        }
    }
})

object Template_1 : Template({
    id("Template")
    name = "template"

    vcs {
        root(BuildoneRoot, "tet->tet")
    }

    steps {
        script {
            id = "RUNNER_194"
            scriptContent = "echo build one"
        }
    }

    triggers {
        vcs {
            id = "vcsTrigger"
        }
    }

    dependencies {
        snapshot(BuildTwoSameVcs) {
        }
    }
})

object BuildoneRoot : GitVcsRoot({
    name = "buildone_root"
    url = "https://github.com/innayan/buildone"
    branchSpec = "+:*"
})

object BuildtwoRoot : GitVcsRoot({
    name = "buildtwo_root"
    url = "https://github.com/innayan/buildtwo"
    branchSpec = "+:*"
})
