package se09.device.service.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class VerneMQSubscribeDTO(
        @JsonProperty("client_id")
        override val username: String,

        @JsonProperty("username")
        override val fullClientId: String,

        val topics: List<MQTTTopicDTO>
): VerneMQEventDTO {

    fun deviceIdTopic(): String {
        val topicStr = topics.first().topic
        return if(topicStr.contains("/")) topicStr.substringBefore("/")
        else topicStr
    }

}
