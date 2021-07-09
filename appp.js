const express = require('express')
const app = express()
const mongoClient = require('mongodb').MongoClient

const url = "mongodb://localhost:27017"//몽고디비의

app.use(express.json())//enabling json parsing in express

mongoClient.connect(url, (err, db) => {//url to server,two parameter of callback function

    if (err) {
        console.log("Error while connecting mongo client")
    } else {

        const myDb = db.db('myDb')//myDb라는 디비
        const collection = myDb.collection('myTable')

        app.post('/signup', (req, res) => {

            const newUser = {
                name: req.body.name,
                email: req.body.email,
                password: req.body.password
            }

            const query = { email: newUser.email }//뉴 유저의

            collection.findOne(query, (err, result) => {//이메일 있는지

                if (result == null) {//디비에
                    collection.insertOne(newUser, (err, result) => {//디비에
                        res.status(200).send()
                    })
                } else {
                    res.status(400).send()
                }

            })

        })

        app.post('/login', (req, res) => {

            const query = {
                email: req.body.email,
                password: req.body.password
            }

            collection.findOne(query, (err, result) => {

                if (result != null) {

                    const objToSend = {//유저 정보 찾아진거니까 거기의 이름과 이메일 리턴
                        name: result.name,
                        email: result.email
                    }

                    res.status(200).send(JSON.stringify(objToSend))//스트링으로 바꿔 보냄.

                } else {
                    res.status(404).send()
                }

            })

        })

    }

})

app.listen(443, () => {
    console.log("Listening...")
})
