
import vorpal from 'vorpal'

import { registerUser, encrypt, loginUser } from './functions'

const cli = vorpal()
let loggedIn = false

cli.delimiter('Input:')

const register = cli.command('register <username> <password>')
register
  .description('registers a username with a password')
  .action((args, cb) => {
    let result = false
    return (
      Promise.resolve(encrypt(args.password) !== undefined)
        .then((hashword) => result = registerUser(args.username, args.password))
          .then((result) => {
            result === true
              ? console.log('Registration Successful')
              : console.log('Registration Failed')
          })
    )
  })

const login = cli.command('login <username> <password>')
login
.description('logs in with a specified username with a password')
.action((args, cb) => {
  return (
    Promise.resolve(encrypt[args.password] !== undefined)
      .then((hashword) => loggedIn = loginUser(args.username, hashword))
        .then((result) => {
          result === true
            ? console.log('Logged in Successfully')
            : console.log('Login failed. Incorrect combination')
        })
  )
})
cli.show()
