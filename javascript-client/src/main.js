import net from 'net'
import vorpal from 'vorpal'

function registerUser (user) {
  let server = net.createConnection(667, () => {
    server.write('hello!\n')
  })
}
const cli = vorpal()

cli.delimiter('Input:')

const register = cli.command('register <username> <password>')
register
  .description('registers a username with a password')
  .action((args, cb) => {
    registerUser(args.username)
    cb()
  })
cli.show()
