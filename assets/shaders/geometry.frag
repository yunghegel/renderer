

in vec3 v_position;
layout(location = 0) out vec4 g_position;

#if defined(NORMAL_ATTRIBUTE) || defined(NORMAL_TEXTURE)
layout(location = 1) out vec4 g_normal;
    #if defined(NORMAL_ATTRIBUTE)
    in vec3 v_normal;
    #endif
    #if defined(NORMAL_TEXTURE)
uniform sampler2D u_normalTexture;
    #endif
#endif

#if defined(COLOR_ATTRIBUTE) || defined(COLOR_TEXTURE)
layout(location = 2) out vec4 g_color;
    #if defined(COLOR_ATTRIBUTE)
    in vec4 v_color;
    #endif
    #if defined(COLOR_TEXTURE)
    uniform sampler2D u_colorTexture;
    #endif
#endif

#if defined(UV_ATTRIBUTE)
in vec2 v_texCoord0;
layout(location = 4) out vec4 g_uv;
#endif

#if defined(METALLIC_ROUGHNESS_TEXTURE) || defined(SPECULAR_TEXTURE)
layout(location = 3) out vec4 g_metallicRoughness;
    #if defined(METALLIC_ROUGHNESS_TEXTURE)
    uniform sampler2D u_metallicRoughnessTexture;
    #elif defined(SPECULAR_TEXTURE)
    uniform sampler2D u_specularTexture;
    #endif
#endif

layout(location = 5) out vec4 g_depth;















out vec4 FragColor;

float linearizeDepth(float depth) {
    float zNear = 0.1;
    float zFar = 100.0;
    return (2.0 * zNear) / (zFar + zNear - depth * (zFar - zNear));
}

void main() {
    g_position = vec4(v_position, 1.0);

    #if defined(UV_ATTRIBUTE)
    g_uv = vec4(v_texCoord0, 0.0, 1.0);
        #if defined(NORMAL_TEXTURE)
        g_normal = texture(u_normalTexture, v_texCoord0);
        #elif defined(NORMAL_ATTRIBUTE)
        g_normal = vec4(normalize(v_normal), 1.0);
        #endif

        #if defined(COLOR_TEXTURE)
        g_color = texture(u_colorTexture, v_texCoord0);
        #elif defined(COLOR_ATTRIBUTE)
        g_color = v_color;
        #endif

        #if defined(METALLIC_ROUGHNESS_TEXTURE)
        g_metallicRoughness = texture(u_metallicRoughnessTexture, v_texCoord0);
        #elif defined(SPECULAR_TEXTURE)
        g_metallicRoughness = texture(u_specularTexture, v_texCoord0);
        #endif
    #endif


    float depth = linearizeDepth(gl_FragCoord.z);

//    gl_FragDepth = depth;

    g_depth = vec4(1.0 - depth);







//    FragColor = g_normal;
}
