Project: Simple Sql Engine Checkpoint 3
Instrction: ./doc/
Testcase: ./testcase/
Library: lib/

Comment: checkpoint 3 is based on checkpoint2, and need further optimization in order to pass some tricky case. An RATree optimizer is created in CP3 to reorder the RATree to make it much more efficient. Since RATree optimizer is good enough to pass almost all case, and due to lacking of time, the index function using berklyDB has not been deploied in CP3.