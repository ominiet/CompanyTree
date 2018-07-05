# CompanyTree


This is a tool used to build, format and view company tree information

Given a list of company names, parents, and a list of top level companies, this tool will build a set of trees
and allow the user to compare and analyze the tree data easily

Setup
=

1. Build the project by running    
`./gradle build`

2. Run the application using  
`./gradlew run `

### How to use ###
1. The program will build all the trees using information from the database

2. You will then be able to either perform operations on that set of trees,
or narrow the list of trees you wish to check. You can do this by selecting the _Fuzzy Search_ option

3. Subsequent operations will be performed on the narrowed list of trees

4. You can always reset to the original set of trees by selecting the _Reset_ option

#### Notes ####


* This tool requires that you have a local instance of MySQL running with the appropriate database
and a user with read access to the database

* On the first run, you will be prompted for the username and password of your 
MySQL user, and then you will begin interactive mode

* Subsequent runs will not require you to reenter your information



