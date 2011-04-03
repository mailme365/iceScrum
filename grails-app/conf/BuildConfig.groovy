/*
 * Copyright (c) 2010 iceScrum Technologies.
 *
 * This file is part of iceScrum.
 *
 * iceScrum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * iceScrum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with iceScrum.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Authors:
 *
 * Vincent Barrier (vincent.barrier@icescrum.com)
 * Stephane Maldini (stephane.maldini@icescrum.com)
 * Manuarii Stein (manuarii.stein@icescrum.com)
 */

import grails.util.GrailsNameUtils

grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.war.file = "target/${appName}.war"

grails.project.war.osgi.headers = false

//grails.plugin.location.'entry-points' =  '../plugins/entry-points'
//grails.plugin.location.'icescrum-core' =  '../plugins/icescrum-core'

coverage {
  enabledByDefault = false
  xml = true
}

grails.project.dependency.resolution = {
  // inherit Grails' default dependencies
  inherits("global") {
    // uncomment to disable ehcache
    // excludes 'ehcache'
  }
  log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
  repositories {
    grailsPlugins()
    grailsCentral()
    grailsHome()

    // uncomment the below to enable remote dependency resolution
    // from public Maven repositories
    //mavenLocal()
    mavenCentral()
    //mavenRepo "http://snapshots.repository.codehaus.org"
    mavenRepo "http://repository.codehaus.org"
    mavenRepo "http://repo.icescrum.org/artifactory/plugins-release/"
  }

//  plugins {
//	runtime ':cached-resources:latest.integration'
//  }
  
  dependencies {
    // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
    test 'xmlunit:xmlunit:1.3'
  }

  grails.war.resources = { stagingDir ->
    copy(todir: "${stagingDir}/WEB-INF/classes/grails-app/i18n") {
      fileset(dir: "grails-app/i18n") {
        include(name: "report*")
      }
    }
  }

  plugins {
      compile "org.icescrum:icescrum-core:1.3.1"
      compile "org.icescrum:entry-points:0.3-BETA"
      compile "org.icescrum:icepush-jquery:0.2"
  }
}

//iceScrum plugins management
def iceScrumPluginsDir = System.getProperty("icescrum.plugins.dir")?:false
println "Compile and use icescrum plugins : ${iceScrumPluginsDir?true:false}"

if (iceScrumPluginsDir){
  iceScrumPluginsDir.split(";").each {
    File dir = new File(it.toString())
    println "Scanning plugin dir : ${dir.canonicalPath}"

    if (dir.exists()){
      File descriptor = dir.listFiles(new FilenameFilter() {
            public boolean accept(File file, String s) {
                return s.endsWith("GrailsPlugin.groovy");
            }
      })[0] ?: null;

      if (descriptor){
        String name = GrailsNameUtils.getPluginName(descriptor.getName());
        println "found plugin : ${name}"
        grails.plugin.location."${name}" =  "${it}"
      }
    }else{
      println "no plugin found in dir"
    }

  }
}