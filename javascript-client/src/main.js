
import vorpal from 'vorpal'

import { registerUser, encrypt, loginUser, compare } from './functions'

const cli = vorpal()
let loggedIn = false

cli.delimiter('Input:')

const register = cli.command('register <username> <password>')
register
  .description('registers a username with a password')
  .action((args, cb) => {
    const hashword = encrypt(args.password)
    return (
    Promise.resolve(hashword !== undefined)
      .then(() => registerUser(args.username, hashword))
        .then((Response) => {
          Response.trueFalse === 'true'
          ? console.log('Successfully registered')
          : console.log('Register failed. Username already taken.')
        })
      )
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

cli.show()
