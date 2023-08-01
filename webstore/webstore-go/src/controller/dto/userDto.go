package dto

// UserDTO struct with data to use as response for login operations
type UserDTO struct {
	Email     string   `json:"email"`
	JWT       string   `json:"jwt"`
	Status    uint64   `json:"status"`
	ExpiresIn uint64   `json:"expiresIn"`
	Name      string   `json:"name"`
	LastName  string   `json:"lastName"`
	Roles     []string `json:"roles"`
}
