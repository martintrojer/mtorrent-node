lt = require("./libtorrent/libtorrent")
console.log(lt.version)

s = new lt.session()
s.listen_on([6881, 6889])

console.log("ole")

//fs = require("fs")
//data = fs.readFileSync("u.torrent")
//ti = new lt.torrent_info(lt.bdecode(data))

ti = new lt.torrent_info("u.torrent")

console.log("dole")
th = s.add_torrent({"ti": ti})
console.log("doff")
