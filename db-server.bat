java -cp tool/h2-2.1.214.jar org.h2.tools.Server -webAllowOthers -tcpAllowOthers
:: you need use a local connection first time to create a database and then you can use remote connection
:: from local and web: jdbc:h2:tcp://localhost/~/db-test