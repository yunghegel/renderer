
layout(location = 0) in vec3 a_position;
out vec3 v_position;

#if defined(NORMAL_ATTRIBUTE)
layout(location = 1) in vec3 a_normal;
out vec3 v_normal;
#endif

#if defined(UV_ATTRIBUTE)
layout(location = 2) in vec2 a_texCoord0;
out vec2 v_texCoord0;
#endif

#if defined(COLOR_ATTRIBUTE)
layout(location = 3) in vec4 a_color;
out vec4 v_color;
#endif

#if defined(COLOR_TEXTURE)
uniform sampler2D u_colorTexture;
#endif






uniform mat4 u_projTrans;
uniform mat4 u_worldTrans;

void main() {

    vec3 position = vec3(u_worldTrans * vec4(a_position.xyz, 1.0)).xyz;

    v_position = vec3(position.xyz);


    #if defined(NORMAL_ATTRIBUTE)
        v_normal = a_normal;
    #endif

    #if defined(UV_ATTRIBUTE)
        v_texCoord0 = a_texCoord0;
    #endif

    gl_Position = u_projTrans * u_worldTrans * vec4(a_position, 1.0);
}
