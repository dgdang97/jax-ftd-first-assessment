
import vorpal from 'vorpal'

import { registerUser, encrypt } from './functions'

const cli = vorpal()
let loggedIn = false

cli.delimiter('Input:')

const register = cli.command('register <username> <password>')
register
  .description('registers a username with a password')
  .action((args, cb) => {
    let result = false
    return (
      Promise.resolve(encrypt[args.password] !== undefined)
        .then((hashword) => result = registerUser(args.username, hashword))
          .then((result) => {
            result === true
              ? console.log('Registration Successful')
              : console.log('Registration Failed')
          })
    )
  })

cli.show()
