import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocalAssociationProvince } from '../local-association-province.model';
import { LocalAssociationProvinceService } from '../service/local-association-province.service';

@Component({
  templateUrl: './local-association-province-delete-dialog.component.html',
})
export class LocalAssociationProvinceDeleteDialogComponent {
  localAssociationProvince?: ILocalAssociationProvince;

  constructor(protected localAssociationProvinceService: LocalAssociationProvinceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.localAssociationProvinceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
