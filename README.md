# gradle-mesh

Gradle Plugin for [Genitcs Mesh](https://getmesh.io/) Headless CMS.

## Goals

**Work in Progress**

This is a proof of concept for interacting with a Gentics Mesh server. The initial MVP prototype will explore concepts to:

* Define and merge schemas/microshemas definitions
* Publish and release nodes to server
* Read nodes from the server and wire up results to task inputs/outputs

## Current Features

Currently this plugin only supports simple server configuration and login. In addition, it provides a default
`MeshTask` type which can be used to execute commands on the server. This task is guaranteed to run after
the client is fully initialized and logged in.

## Getting Started

Until the plugin is stable and a release published to the Gradle plugin portal, one must add the project directly
to your own project sources, then consume it similar to the following:

*settings.gradle*

```
pluginManagement {
    repositories {
        mavenCentral()
        jcenter()
        maven { url = "https://maven.gentics.com/maven2" }
    }
}

rootProject.name = 'website'

includeBuild "./gradle-mesh"
```

*build.gradle*

```
import io.waweb.mesh.gradle.tasks.*
 
plugins {
	id 'base'
	id 'io.waweb.mesh'
}

mesh {
	host = "localhost"
	port = 8080
	useSsl = false
	userName = "admin"
	password = "admin"
	// Or use a secure token instead...
	// apiKey = "SECURE_API_KEY"
}

task helloMesh(type: MeshTask) {}

build {
	dependsOn helloMesh
}
```

The configuration may also be set with the following environment variables:

* `GRADLE_MESH_HTTP_HOST`
* `GRADLE_MESH_HTTP_PORT`
* `GRADLE_MESH_HTTP_SSL_ENABLE`
* `GRADLE_MESH_API_KEY`
* `GRADLE_MESH_USER`
* `GRADLE_MESH_PASS`

Please refer to the [Gentics Mesh Documentation](https://getmesh.io/docs/api/#users__userUuid__token_post) for more info on how to request an API key.
