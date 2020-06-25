mkdir executables
cd Client
chmod +x mvnw
./mvnw clean package
cp target/Client-0.0.1-SNAPSHOT.jar ../executables
cd ..
cd Server
chmod +x mvnw
./mvnw clean package
cp target/Server-0.0.1-SNAPSHOT.war ../executables
cd ..
cd GameServer
chmod +x mvnw
./mvnw clean package
cp target/GameServer-1.0-SNAPSHOT.jar ../executables
cd ..
cd executables
mv Client-0.0.1-SNAPSHOT.jar Client17.jar
mv Server-0.0.1-SNAPSHOT.war Server17.war
mv GameServer-1.0-SNAPSHOT.jar GameServer17.jar
