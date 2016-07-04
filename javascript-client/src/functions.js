import net from 'net'
import bcrypt from 'bcryptjs'

function serverConnect (command, user, hashword) {
  return new Promise(function executor (resolve, reject) {
    let server = net.createConnection(667, '127.0.0.1', (err) => {
      if (err) {
        return false
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
  return new Promise(function executor (resolve, reject) {
    let server = net.createConnection(667, '127.0.0.1', (err) => {
      if (err) {
        reject(err)
      } else {
        server.write(`listFiles\n${JSON.stringify({User: {user: user}})}\n`)
        server.on('data', (data) => {
          let { FileData } = JSON.parse(data.toString())
          if (FileData.filePath === 'Stop') {
            resolve()
          } else {
            console.log(`File ID: ${FileData.fileID} | File Path: ${FileData.filePath}`)
            server.write('go\n')
          }
        })
      }
    })
  })
}

export function uploadFiles (user, localpath, newpath) {
  return new Promise(function executor (resolve, reject) {
    let server = net.createConnection(667, '127.0.0.1', (err) => {
      if (err) {
        reject(err)
      } else {
        server.write(`uploadFile\n${JSON.stringify({FileData: {username: user, filePath: localpath, altPath: newpath}})}\n`)
        server.on('data', (data) => {
          const { Response } = JSON.parse(data.toString())
          resolve(Response)
        })
      }
    })
  })
}

export function downloadFiles (user, fileID, newpath) {
  return new Promise(function executor (resolve, reject) {
    let server = net.createConnection(667, '127.0.0.1', (err) => {
      if (err) {
        reject(err)
      } else {
        server.write(`downloadFile\n${JSON.stringify({FileData: {username: user, fileID: fileID, altPath: newpath}})}\n`)
        server.on('data', (data) => {
          const { Response } = JSON.parse(data.toString())
          resolve(Response)
        })
      }
    })
  })
}

export function encrypt (password) {
  const salt = bcrypt.genSaltSync(10)
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
  listFiles,
  encrypt,
  uploadFiles,
  downloadFiles,
  compare
}
