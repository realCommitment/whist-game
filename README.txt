We were uncertain on where exactly you wanted the whist.properties file.

***

Currently the code will access the whist.properties file in the Whist folder (root)


***

If you want the code to access the  whist.properties file in the swen30006 folder then please do the following:

Open Whist/swen30006/game/PropertyReaderSingleton.java
Got to line 44
Change "whist.properties" to "swen300006/whist.properties"