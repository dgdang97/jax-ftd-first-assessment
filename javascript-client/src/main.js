
import vorpal from 'vorpal'

import { registerUser, encrypt, loginUser } from './functions'

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
  const hashword = encrypt(args.password)
  return (
  Promise.resolve(hashword !== undefined)
    .then(() => loginUser(args.username, hashword))
      .then((Response) => {
        if (Response.trueFalse === 'true') {
          loggedIn = true
          console.log('Successfully logged in')
        } else {
          console.log('Register failed. Username already taken.')
        }
      })
    )
})

cli.show()
