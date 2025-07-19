package com.letrasypapeles.backend.controller;




// @ExtendWith(MockitoExtension.class)
// public class AuthenticationControllerTest {

//   @Mock
//     private AuthenticationManager authenticationManager;

//     @Mock
//     private JwtGenerator jwtGenerator;

//     @Mock
//     private UserRepository userRepository;

//     @Mock
//     private RoleRepository roleRepository;

//     @Mock
//     private PasswordEncoder passwordEncoder;

//     @Mock
//     private Authentication authentication;

//     @Mock
//     private SecurityContext securityContext;

//     @InjectMocks
//     private AuthenticationController authenticationController;

//     prvate AuthenticationResponse authenticationResponse;
//     private AuthenticationRequest authenticationRequest;
//     private BaseUser user;


//     @BeforeEach
//     public void setUp() {
//       authenticationRequest = new AuthenticationRequest();
//       authenticationRequest.setUsername("testuser");
//       authenticationRequest.setPassword("password");

//       authenticationRequest = new AuthenticationRequest();
//       authenticationRequest.setUsername("newuser");
//       authenticationRequest.setPassword("password");
//       authenticationRequest.setName("Test User");
//       authenticationRequest.setEmail("test@email.com");

//       user = new BaseUser();
//       user.setUsername("newuser");

//       role = new Role();
//       role.setName("CLIENT");

//       SecurityContextHolder.setContext(securityContext);
//     }

//     @Test
//     public void testLoginSuccess() {
//       // Given
//       String expectedToken = "jwt-token";
//       when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//           .thenReturn(authentication);
//       when(jwtGenerator.generateToken(authentication)).thenReturn(expectedToken);

//       // When
//       ResponseEntity<AuthenticationResponse> response = authenticationController.login(authenticationRequest);

//       // Then
//       assertEquals(HttpStatus.OK, response.getStatusCode());
//       assertNotNull(response.getBody());
//       assertEquals(expectedToken, response.getBody().getAccessToken());
      
//       verify(securityContext).setAuthentication(authentication);
//       verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
//       verify(jwtGenerator).generateToken(authentication);
//     }

//     @Test
//     public void testRegisterSuccess() {

//       when(userRepository.existsByUsername(authenticationRequest.getUsername())).thenReturn(false);
//       when(passwordEncoder.encode(authenticationRequest.getPassword())).thenReturn("encoded-password");
      
//       when(roleRepository.findByRoleName("CLIENT")).thenReturn(Optional.of(role));
//       when(userRepository.save(any(BaseUser.class))).thenReturn(user);

//       ResponseEntity<?> response = authenticationController.registro(authenticationResponse);

//       assertEquals(HttpStatus.CREATED, response.getStatusCode());
//       assertEquals("Usuario registrado de forma exitosa.", response.getBody());
      
//       verify(userRepository).existsByUsername(authenticationRequest.getUsername());
//       verify(passwordEncoder).encode(authenticationRequest.getPassword());
//       // verify(roleRepository).findByRoleName("CLIENTE");
//       verify(userRepository).save(any(BaseUser.class));
//     }

//     @Test
//     public void testRegisterUserAlreadyExists() {
//       when(userRepository.existsByUsername(authenticationRequest.getUsername())).thenReturn(true);
//       ResponseEntity<?> response = authenticationController.registro(authenticationRequest);
//       assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//       assertEquals("El usuario ya existe", response.getBody());
      
//       verify(userRepository).existsByUsername(authenticationRequest.getUsername());
//       verify(userRepository, never()).save(any(BaseUser.class));
//     }
  
// }

