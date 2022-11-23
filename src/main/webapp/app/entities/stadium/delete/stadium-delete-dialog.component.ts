import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStadium } from '../stadium.model';
import { StadiumService } from '../service/stadium.service';

@Component({
  templateUrl: './stadium-delete-dialog.component.html',
})
export class StadiumDeleteDialogComponent {
  stadium?: IStadium;

  constructor(protected stadiumService: StadiumService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.stadiumService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
