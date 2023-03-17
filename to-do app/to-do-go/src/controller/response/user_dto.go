package response

type UserDTO struct {
	Email     string `json:"email"`
	JWT       string `json:"jwt"`
	ID        uint64 `json:"id"`
	Status    uint64 `json:"status"`
	ExpiresIn uint64 `json:"expiresIn"`
	Name      string `json:"name"`
	LastName  string `json:"lastName"`
}
