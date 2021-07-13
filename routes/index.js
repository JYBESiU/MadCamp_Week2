module.exports = function(app, UserAccount, Battle){

  app.post('/signupkakao', (req, res)=>{
    var userAccount = new UserAccount();
    userAccount.name = req.body.name;
    userAccount.id = req.body.id;
    userAccount.password = "kakaologin";
    userAccount.online = "true"
    userAccount.imgnumber =req.body.imgnumber
    userAccount.win = 0
    userAccount.lose =0

    const query = { id: userAccount.id }//뉴 유저의

    UserAccount.findOne(query, (err, result) => {//이메일 있는지
        if (result == null) {//디비에
          userAccount.save(function(err){
            if(err){
              console.error(err);
              return res.status(400).send();
            }
            console.error("newer");
            return res.status(200).send()
            })
        }
        else if(result){
          result.online="true"
          result.save((err)=>{
            if(err){
              console.error(err);
              return res.status(400).send();
            }
            console.error("로그인");

            res.status(200).send()
          })
          console.error("있음");
          return res.status(200).send()
        }
        else if(err){
          console.error(err);
          return res.status(400).send();

        }
    })

  })

  app.post('/signup', (req, res)=>{
    var userAccount = new UserAccount();
    userAccount.name = req.body.name;
    userAccount.id = req.body.id;
    userAccount.password = req.body.password;
    userAccount.online = "false"
    userAccount.imgnumber ="1"
    userAccount.win = 0
    userAccount.lose =0

    const query = { id: userAccount.id }//뉴 유저의

    UserAccount.findOne(query, (err, result) => {//이메일 있는지
        if (result == null) {//디비에
          userAccount.save(function(err){
            if(err){
              console.error(err);
              return res.status(400).send();
            }
            res.status(200).send()
          })
        }
    })
  })

  app.post('/login', (req, res)=>{
    UserAccount.findOne({id: req.body.id, password:req.body.password }, (err, result) => {
      if(err){
        console.log("에러")
        return res.status(404).send();
      }
      if(!result){
        console.log("없음")
        return res.status(404).send();
      }
      else{
        const objToSend = {//유저 정보 찾아진거니까 거기의 이름과 이메일 리턴
          name: result.name,
          id: result.id
        }
        console.log("있음")
        result.online = "true"

        result.save((err)=>{
          if(err) return res.status(404).send();
          res.status(200).send(JSON.stringify(objToSend))//스트링으로 바꿔 보냄.
        })
      }
    })
  })

  app.post('/online', (req, res)=>{
    UserAccount.find({$or:[{online : "true"}, {online:"playing"}]}, (err, result)=>{
      if(err){
        console.log("에러")
        return res.status(404).send();
      }
      if(!result){
        console.log("없음")
        return res.status(404).send();
      }
      else{
        console.log(result)
        res.status(200).send(JSON.stringify(result))//스트링으로 바꿔 보냄.
      }
    })
  })

  app.post('/userinfo', (req, res)=>{
    const query = { id: req.body.id }//뉴 유저의

    UserAccount.findOne(query, (err, result) =>{
      if(result!=null){
        return res.status(200).send(JSON.stringify(result))//스트링으로 바꿔 보냄.
      }
      if(err){
        return res.status(404).send()
      }
    })
  })

  app.post('/change', (req, res)=>{
    const query = { id: req.body.id }//뉴 유저의

    UserAccount.findOne(query, (err, result) =>{
      if(result!=null){
        result.name = req.body.name
        result.password = req.body.password
        result.imgnumber = req.body.imgnumber

        result.save((err)=>{
          if(err) return res.status(404).send();
          console.log("saved")
          return res.status(200).send()
        })
      }
      if(err){
        return res.status(404).send()
      }
    })
  })

  app.post('/logout', (req, res)=>{
    const query = { id: req.body.id }//뉴 유저의

    UserAccount.findOne(query, (err, result) =>{
      if(result!=null){
        result.online = "false"
        result.save((err)=>{
          if(err) return res.status(404).send();
          console.log("logout")
          return res.status(200).send()
        })
      }
      if(err){
        return res.status(404).send()
      }
    })
  })

  app.post('/rank', (req, res)=>{
    UserAccount.find({}, (err, result)=>{
      if(err){
        console.log("에러")
        return res.status(404).send();
      }
      if(!result){
        console.log("없음")
        return res.status(404).send();
      }
      else{
        console.log(result)
        res.status(200).send(JSON.stringify(result))//스트링으로 바꿔 보냄.
      }
    })
  })

  app.post('/getbattle', (req, res)=>{
    var id = req.body.id

    Battle.find({$or:[{ask : id}, {accept:id}]}, (err, result)=>{
      if(result){
        console.log(result)
        return res.status(200).send(JSON.stringify(result))//스트링으로 바꿔 보냄.
      }
      else if(err){
        if(err) return res.status(404).send();

      }
    })
  })

  app.post('/changeplay', (req,res)=>{
    const query = { id: req.body.id }//뉴 유저의

    UserAccount.findOne(query, (err, result) =>{
      if(result!=null){
        result.online = "playing"
        result.save((err)=>{
          if(err) return res.status(404).send();
          console.log("playing"+req.body.id)
          return res.status(200).send()
        })
      }
      if(err){
        return res.status(404).send()
      }
    })
  })

  app.post('/changeonline', (req,res)=>{
    const query = { id: req.body.id }//뉴 유저의

    UserAccount.update(query, {online : "true"});

    UserAccount.findOne(query, (err, result) =>{
      if(result.online = "true"){
        console.log("true"+req.body.id)
        return res.status(200).send()
      }
      if (err){
        return res.status(404).send()
      }
    })
  })

  app.post('/winLose', (req,res)=>{
    var id = req.body.id
    var result = req.body.result

    if(result=="win"){
      UserAccount.findOne({id:id}, (err, result)=>{
        if(result){
          var prev = result.win
          result.win = prev+1
          result.online="true"

          result.save((err)=>{
            if(err) return res.status(404).send()
            return res.status(200).send()
          })
        }
        if(err) return res.status(404).send()
        if(!result) return res.status(400).send()
      })
    }

    if(result=="lose"){
      UserAccount.findOne({id:id}, (err, result)=>{
        if(result){
          var prev = result.lose
          result.lose = prev+1
          result.online="true"

          result.save((err)=>{
            if(err) return res.status(404).send()
            return res.status(200).send()
          })
        }
        if(err) return res.status(404).send()
        if(!result) return res.status(400).send()
      })
    }

    if(result==""){
      UserAccount.findOne({id:id}, (err, result)=>{
        if(result){
          result.online="true"

          result.save((err)=>{
            if(err) return res.status(404).send()
            return res.status(200).send()
          })
        }
        if(err) return res.status(404).send()
        if(!result) return res.status(400).send()
      })
    }

    })

}
