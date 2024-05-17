package handler

type LoadBalance interface {
}

var _ LoadBalance = (*loadBalancer)(nil)

type loadBalancer struct {
}
