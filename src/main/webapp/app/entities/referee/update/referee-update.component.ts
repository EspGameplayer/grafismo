import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IReferee, Referee } from '../referee.model';
import { RefereeService } from '../service/referee.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { ILocalAssociationProvince } from 'app/entities/local-association-province/local-association-province.model';
import { LocalAssociationProvinceService } from 'app/entities/local-association-province/service/local-association-province.service';

@Component({
  selector: 'jhi-referee-update',
  templateUrl: './referee-update.component.html',
})
export class RefereeUpdateComponent implements OnInit {
  isSaving = false;

  peopleCollection: IPerson[] = [];
  localAssociationProvincesSharedCollection: ILocalAssociationProvince[] = [];

  editForm = this.fb.group({
    id: [],
    person: [null, Validators.required],
    association: [],
  });

  constructor(
    protected refereeService: RefereeService,
    protected personService: PersonService,
    protected localAssociationProvinceService: LocalAssociationProvinceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ referee }) => {
      this.updateForm(referee);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const referee = this.createFromForm();
    if (referee.id !== undefined) {
      this.subscribeToSaveResponse(this.refereeService.update(referee));
    } else {
      this.subscribeToSaveResponse(this.refereeService.create(referee));
    }
  }

  trackPersonById(_index: number, item: IPerson): number {
    return item.id!;
  }

  trackLocalAssociationProvinceById(_index: number, item: ILocalAssociationProvince): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReferee>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(referee: IReferee): void {
    this.editForm.patchValue({
      id: referee.id,
      person: referee.person,
      association: referee.association,
    });

    this.peopleCollection = this.personService.addPersonToCollectionIfMissing(this.peopleCollection, referee.person);
    this.localAssociationProvincesSharedCollection = this.localAssociationProvinceService.addLocalAssociationProvinceToCollectionIfMissing(
      this.localAssociationProvincesSharedCollection,
      referee.association
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query({ filter: 'referee-is-null' })
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleCollection = people));

    this.localAssociationProvinceService
      .query()
      .pipe(map((res: HttpResponse<ILocalAssociationProvince[]>) => res.body ?? []))
      .pipe(
        map((localAssociationProvinces: ILocalAssociationProvince[]) =>
          this.localAssociationProvinceService.addLocalAssociationProvinceToCollectionIfMissing(
            localAssociationProvinces,
            this.editForm.get('association')!.value
          )
        )
      )
      .subscribe(
        (localAssociationProvinces: ILocalAssociationProvince[]) =>
          (this.localAssociationProvincesSharedCollection = localAssociationProvinces)
      );
  }

  protected createFromForm(): IReferee {
    return {
      ...new Referee(),
      id: this.editForm.get(['id'])!.value,
      person: this.editForm.get(['person'])!.value,
      association: this.editForm.get(['association'])!.value,
    };
  }
}
