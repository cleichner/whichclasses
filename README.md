whichclasses
============

A better interface for the University of Arizona TCE system.

    # Install the protobuf compiler. `protoc` should be at /usr/local/bin/protoc

    # On Linux:
    sudo apt-get install protobuf-compiler
    # Note: may need to symlink.
    sudo ln -s /usr/bin/protoc /usr/local/bin/protoc

    # Create and edit config before running locally.
    cp src/main/resources/config.properties.EXAMPLE src/main/resources/config.properties
    vi src/main/resources/config.properties

    # Build and run. Maven should download all dependencies.
    mvn compile
    mvn exec:java -Dexec.mainClass="com.whichclasses.WhichClasses"

