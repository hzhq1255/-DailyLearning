package log

import "testing"

func Test_zap_info(t *testing.T) {
	log := newZapLogger(LevelInfo)
	log.Infow("Info msg", "num", 1)
	log.Infof("Info msg num is %d", 1)
}

func Test_global_log(t *testing.T) {
	Infof("Info msg %d %s", 1, "d")
}
