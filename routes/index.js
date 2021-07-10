module.exports = function(app, Battle) {
  app.get('/battles', (req, res) => {
    Battle.find({accept: req.body.accept}, (err, battles) => {
      if(err) return res.status(500).send({error: 'database failure'})
      else {
        console.log("find accept!", battles)
        res.status(200).send("success!")
      }

    })
  })

  app.post('/battles', (req, res) => {
    var battle = new Battle()
    battle.ask = req.body.ask
    battle.accept = req.body.accept

    battle.save((err) => {
      if (err) {
        console.log("err" + err)
        return res.status(404).send()
      }
    })
    res.status(200).send()
    console.log("battle save!", req.body.ask, req.body.accept)
  })
}
