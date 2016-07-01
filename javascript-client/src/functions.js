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
          resolve(Response)
        })
      }
    })
  })
}

export function registerUser (user, hashword) {
  return serverConnect('registerUser', user, hashword)
}

export function loginUser (user) {
  return serverConnect('loginUser', user, null)
}

export function listFiles (user) {
  return serverConnect('listFiles', user, null)
}

export function uploadFiles (user, localpath, newpath) {
  return new Promise(function executor (resolve, reject) {
    let server = net.createConnection(667, (err) => {
      if (err) {
        return false
      } else {
        server.write(`uploadFile\n${JSON.stringify({User: {user: user}})}\n${JSON.stringify({FileData: {filePath: localpath, altPath: newpath}})}`)
        server.on('data', (data) => {
          const { Response } = JSON.parse(data.toString())
          resolve(Response)
        })
      }
    })
  })
}

export function encrypt (password) {
  return bcrypt.hashSync(password, salt)
}

export function compare (plaintextPassword, hashedPassword) {
  return new Promise(function executor (resolve, reject) {
    bcrypt.compare(plaintextPassword, hashedPassword, function (err, successFlag) {
      if (err) {
        reject(err)
      } else {
        resolve(successFlag)
      }
    })
  })
}

export default {
  registerUser,
  loginUser,
  encrypt,
  uploadFiles,
  compare
}
