use user_service
db.createUser({ user: "root", pwd: "pass@word1", roles: [{ role: "dbOwner", db: "user_service" }] })

c:\>mongodump --db user_service --out c:\user_service.json


{
  "userName" : "admin@admin",
  "password" : "admin",
  "firstName" : "Admin",
  "lastName" : "Admin",
  "contactNumber" : 9434580584,
  "role" : "ADMIN" 
}

{
  "userName" : "arnabmca2006@gmail.com",
  "password" : "arnabray",
  "firstName" : "Arnab",
  "lastName" : "Ray",
  "contactNumber" : 9434580584,
  "role" : "USER"
}
{
  "userName" : "sangita_adak2006@gmail.com",
  "password" : "sangitaadak",
  "firstName" : "Sangita",
  "lastName" : "Adak",
  "contactNumber" : 9433764150,
  "role" : "USER"
}
{
  "userName" : "abhipsa2011@gmail.com",
  "password" : "abhipsaray",
  "firstName" : "Abhipsa",
  "lastName" : "Ray",
  "contactNumber" : 9434580584,
  "role" : "USER"
}

{
  "userName" : "arnabmca2006@rediffmail.com",
  "password" : "sureshkumar",
  "firstName" : "Suresh",
  "lastName" : "Kumar",
  "contactNumber" : 9434580584,
  "role" : "MENTOR",  
  "linkedinUrl" : "suresh_kumar@linkline.com",
  "yearsOfExperience" : 12
}
{
  "userName" : "amit_kumar@gmail.com",
  "password" : "amitkumar",
  "firstName" : "Amit",
  "lastName" : "Kumar",
  "contactNumber" : 9434580584,
  "role" : "MENTOR",  
  "linkedinUrl" : "amit_kumar@linkline.com",
  "yearsOfExperience" : 10
}