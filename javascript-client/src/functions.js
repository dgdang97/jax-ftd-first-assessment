import net from 'net'
import bcrypt from 'bcryptjs'
const salt = bcrypt.genSaltSync(10)

function serverConnect (command, user, hashword) {
  return new Promise(function executor (resolve, reject) {
    let server = net.createConnection(667, (err) => {
      if (err) {
        return false // reject(err)
      } else {
        server.write(`${command}\n${JSON.stringify({User: {user: user, password: hashword}})}\n`)
        server.on('data', (data) => {
          const { Response } = JSON.parse(data.toString())
          if (Response.trueFalse === 'true') {
            resolve(Response)
          } else {
            reject(err)
          }
        })
      }
    })
  })
}

export function registerUser (user, hashword) {
  return serverConnect('registerUser', user, hashword)
}

export function loginUser (user, hashword) {
  serverConnect('loginUser', user, hashword)
}

export function encrypt (password) {
  return bcrypt.hashSync(password, salt)
}

export default {
  registerUser,
  loginUser,
  encrypt
}
