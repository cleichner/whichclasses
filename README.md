whichclasses
============

A better interface for the University of Arizona TCE system.

    # Create and edit config before running locally.
    cp src/main/resources/config.properties.EXAMPLE src/main/resources/config.properties
    vi src/main/resources/config.properties

    # Build and run. Maven should download all dependencies.
    mvn compile
    mvn exec:java -Dexec.mainClass="com.whichclasses.WhichClasses"

