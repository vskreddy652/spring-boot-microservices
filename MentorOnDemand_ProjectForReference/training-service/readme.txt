use training_service
db.createUser({ user: "root", pwd: "pass@word1", roles: [{ role: "dbOwner", db: "training_service" }] })

c:\>mongodump --db training_service --out c:\training_service.json
