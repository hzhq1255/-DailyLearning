namespace go hello.world

service HelloService {
    string Hello(1: string name);
    // add new bye func
    string Bye(1: string name);
}