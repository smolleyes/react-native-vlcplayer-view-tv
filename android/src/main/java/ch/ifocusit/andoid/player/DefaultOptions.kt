package ch.ifocusit.andoid.player

import org.videolan.libvlc.util.VLCUtil

class DefaultOptions {

    companion object {
        fun options(): MutableList<String> {
            return options(emptyList())
        }

        fun options(initOptions: List<String>): MutableList<String> {
            val options = ArrayList<String>()

            options.add("-v")
            options.add("--freetype-background-opacity=0")
            options.add("--freetype-bold")
            options.add("--freetype-rel-fontsize=16")
            options.add("--freetype-color=16777215")
            options.add("--no-sout-chromecast-audio-passthrough")
            options.add("--sout-chromecast-conversion-quality=2")
            options.add("--sout-keep")
            options.add("--audio-time-stretch")
            options.add("--avcodec-skiploopfilter")
            options.add("" + getDeblocking(-1))
            options.add("--avcodec-skip-frame")
            options.add("0")
            options.add("--avcodec-skip-idct")
            options.add("0")
            options.add("--subsdec-encoding")
            options.add("")
            options.add("--stats")
            options.add("--android-display-chroma")
            options.add("")
            options.add("--audio-resampler")
            options.add(getResampler())
            options.add("file_crypt,none")

            options.addAll(initOptions)

            return options
        }

        private fun getResampler(): String {
            val m = VLCUtil.getMachineSpecs()
            return if ((m == null || m.processors > 2)) "soxr" else "ugly"
        }

        private fun getDeblocking(deblocking: Int): Int {
            var ret = deblocking
            if (deblocking > 4) {
                return 3
            }
            if (deblocking > 0) {
                return deblocking
            }
            val machineSpecs = VLCUtil.getMachineSpecs() ?: return ret
            // Set some reasonable sDeblocking defaults:
            //
            // Skip all (4) for armv6 and MIPS by default
            // Skip non-ref (1) for all armv7 more than 1.2 Ghz and more than 2 cores
            // Skip non-key (3) for all devices that don't meet anything above
            ret = if ((machineSpecs.hasArmV6 && !(machineSpecs.hasArmV7)) || machineSpecs.hasMips) {
                4
            } else if (machineSpecs.frequency >= 1200 && machineSpecs.processors > 2) {
                1
            } else if (machineSpecs.bogoMIPS >= 1200 && machineSpecs.processors > 2) {
                1
            } else {
                3
            }
            return ret
        }
    }
}