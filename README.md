# shRIOL
This repository contains the OWL and SHACL files commented in the paper "Compliance checking in reified I/O logic via SHACL" submitted to ICAIL 2021.

The subfolder shRIOL contains Java files to execute the SHACL files on the ontology.

To compile: "javac -cp ./src/;./lib/* -d ./class ./src/DetectViolations.java"
To run compiled files: "java -cp ./class;./lib/* DetectViolations"

By executing the Java files, the following messages are printed on screen. See the paper for more details and explanations.
