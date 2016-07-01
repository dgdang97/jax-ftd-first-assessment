
import vorpal from 'vorpal'

import { registerUser, loginUser, listFiles, encrypt, uploadFiles, downloadFiles, compare } from './functions'

const cli = vorpal()
let loggedIn = false
let username = ''

cli.delimiter('Input:')

const register = cli.command('register <username> <password>')
register
  .description('registers a username with a password')
  .action((args, cb) => {
    const hashword = encrypt(args.password)
    if (loggedIn === false) {
      return (
      Promise.resolve(hashword !== undefined)
        .then(() => registerUser(args.username, hashword))
          .then((Response) => {
            Response.trueFalse === 'true'
            ? console.log('Successfully registered')
            : console.log('Register failed. Username already taken.')
          })
        )
    } else {
      console.log('You are already logged in!')
    }
  })

const login = cli.command('login <username> <password>')
login
.description('logs in with a specified username with a password')
.action((args, cb) => {
  return (
  Promise.resolve(
      loggedIn === false
    ? loginUser(args.username)
      .then((Response) => {
        Response.trueFalse === 'true'
        ? compare(args.password, Response.hash)
          .then((correctPassword) => {
            if (correctPassword) {
              loggedIn = true
              username = args.username
              console.log('Successfully logged in.')
            } else {
              console.log('Login failed. Please try again.')
            }
          })
        : console.log('Login failed. Please try again.')
      })
      : console.log('You are already logged in!')
    )
  )
})

const files = cli.command('files')
files
  .action((args, cb) => {
    return (
      Promise.resolve(
        loggedIn === true
        ? listFiles(username)
          .then(() => console.log('End of file list'))
        : console.log('Access denied. Please log in first.')
      )
    )
  })

const upload = cli.command('upload <localpath> [newpath]')
upload
  .action((args, cb) => {
    return (
      Promise.resolve(
        loggedIn === true
        ? uploadFiles(username, args.localpath, args.newpath)
          .then((Response) =>
        Response.trueFalse === 'true'
      ? console.log('Upload successful')
      : console.log('Upload failed')
    )
        : console.log('Access denied. Please log in first.')
      )
    )
  })

const download = cli.command('download <fileID> [newpath]')
download
  .action((args, cb) => {
    return (
        Promise.resolve(
          loggedIn === true
          ? downloadFiles(username, args.fileID, args.newpath)
            .then((Response) =>
          Response.trueFalse === 'true'
        ? console.log('Download successful')
        : console.log('Download failed')
      )
          : console.log('Access denied. Please log in first.')
        )
      )
  })
cli.show()
