import net from 'net'
import bcrypt from 'bcryptjs'
const salt = bcrypt.genSaltSync(10)

function serverConnect (command, user, hashword) {
  console.log(hashword)
  return new Promise(function executor (resolve, reject) {
    let server = net.createConnection(667, (err) => {
      if (err) {
        reject(err)
      } else {
        server.write(command + '\n' + `${JSON.stringify({User: {user: user, password: hashword}})}\n`)
        server.on('data', (data) => {
          resolve(data.toString())
        })
      }
    })
  })
}

export function registerUser (user, hashword) {
  const passHash = encrypt(hashword)
  serverConnect('registerUser', user, passHash)
}

export function loginUser (user, hashword) {
  serverConnect('loginUser', user, hashword)
}

export function encrypt (password) {
  // return bcrypt.hashSync(password, salt)
  console.log('k')
  return new Promise(function executor (resolve, reject) {
    bcrypt.hash(password, salt, function (err, hashedPassword) {
      if (err) {
        console.log('fail')
        reject(err)
      } else {
        console.log('hp' + hashedPassword)
        resolve(hashedPassword)
      }
    })
  })
}

export default {
  registerUser,
  loginUser,
  encrypt
}
