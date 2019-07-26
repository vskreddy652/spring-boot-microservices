use payment_service
db.createUser({ user: "root", pwd: "pass@word1", roles: [{ role: "dbOwner", db: "payment_service" }] })

c:\>mongodump --db payment_service --out c:\payment_service.json


mvn package -Dmaven.test.skip=true
mvn install -Dmaven.test.skip=true
