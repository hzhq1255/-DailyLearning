package log

import "testing"

func Test_zap_info(t *testing.T) {
	log := newZapLogger(LevelInfo)
	log.WithName("test")
	log.Infow("Info msg", "num", 1)
	log.Infof("Info msg num is %d", 1)
}
