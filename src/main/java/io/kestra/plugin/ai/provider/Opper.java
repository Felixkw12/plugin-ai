package io.kestra.plugin.ai.provider;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.kestra.core.models.annotations.Example;
import io.kestra.core.models.annotations.Plugin;
import io.kestra.core.models.property.Property;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import io.kestra.core.models.annotations.PluginProperty;

@Getter
@SuperBuilder
@NoArgsConstructor
@JsonDeserialize
@Schema(
    title = "Use Opper models",
    description = "Connects to the Opper OpenAI-compatible API with API key and model name. Opper is an EU-hosted AI gateway that serves models from many providers behind one API key; `modelName` takes an Opper model ID such as `claude-sonnet-4-6` or `mistral-large-2512` (see https://opper.ai/models). Supports chat completion (including tool calling) and embeddings, subject to the capabilities of the selected model. Image generation is not available."
)
@Plugin(
    examples = {
        @Example(
            title = "Chat completion with Opper",
            full = true,
            code = {
                """
                    id: chat_completion
                    namespace: company.ai

                    inputs:
                      - id: prompt
                        type: STRING

                    tasks:
                      - id: chat_completion
                        type: io.kestra.plugin.ai.completion.ChatCompletion
                        provider:
                          type: io.kestra.plugin.ai.provider.Opper
                          apiKey: "{{ secret('OPPER_API_KEY') }}"
                          modelName: claude-sonnet-4-6
                        messages:
                          - type: SYSTEM
                            content: You are a helpful assistant, answer concisely, avoid overly casual language or unnecessary verbosity.
                          - type: USER
                            content: "{{ inputs.prompt }}"
                    """
            }
        )
    }
)
public class Opper extends OpenAICompliantProvider {
    private static final String BASE_URL = "https://api.opper.ai/v3/compat";

    @Schema(
        title = "API base URL",
        description = "Base URL of the Opper OpenAI-compatible API. Defaults to `https://api.opper.ai/v3/compat`.",
        example = "https://api.opper.ai/v3/compat"
    )
    @Builder.Default
    @PluginProperty(group = "connection")
    private Property<String> baseUrl = Property.ofValue(BASE_URL);
}
