import net from 'net'
import bcrypt from 'bcryptjs'

function serverConnect (command, string) {
  return new Promise(function executor (resolve, reject) {
    let server = net.createConnection(667, (err) => {
      if (err) {
        reject(err)
      } else {
        server.write(command + '\n')
        resolve(server)
        server.write(string)
        server.on('data', (data) => {
          resolve(data.toString())
        })
      }
    })
  })
}

export function registerUser (user, hashword) {
  serverConnect('RegisterUser', `${JSON.stringify({RegisterUser: {user: user, password: hashword}})}\n`)
}

export function encrypt (password) {
  return new Promise(function executor (resolve, reject) {
    bcrypt.genSalt((err, salt) => {
      if (err) {
        reject(err)
      } else {
        bcrypt.hash(password, salt, function (err, hashedPassword) {
          if (err) {
            reject(err)
          } else {
            resolve(hashedPassword)
          }
        })
      }
    })
  })
}

export default {
  registerUser,
  encrypt
}
