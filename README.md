#Task

Network class has the most important methods:<br>
add: add a network, if not possible return fasle and dont add anything. If possible merge the trees together.<br>
list: returns a list of all ips in the netwok (ips sorted)<br>
connect: connects to existent ips. if not possible return false and dont connect them<br>
disconnect: disconnects a connection between two ips. If the network is left without a valide tree (root + at least one child) then return false and dont disconnect.<br>
contains: return true if network contains that ip <br>
getHeight: return the height ("root" has height of == 0) of the tree using the given ip as the "root"<br> 
geLevels: returns each "level" of the tree as a list using the given ip as the "root" <br>
geRoute: returns the shortest path between two ips<br>
toString: Bracket representation of a tree using the give ip as the "root"<br><br>
a network can either be created with an IP "root" and a List of its childre or a bracketNotation.<br>
bracketnotation: (100.2.1.3 (2.1.2.4 2.1.2.5) 5.5.5.5) <- example<br>
"(" signifies that there is a node that has "children". The ip right next to the bracket is the "root" and the following ips (including the next ip that is after a "(" cuz its still a child of that one. Just to keep in mind) are its children
