import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPerson, Person } from '../person.model';
import { PersonService } from '../service/person.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';

@Component({
  selector: 'jhi-person-update',
  templateUrl: './person-update.component.html',
})
export class PersonUpdateComponent implements OnInit {
  isSaving = false;

  countriesSharedCollection: ICountry[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    surname1: [],
    surname2: [],
    nickname: [],
    graphicsName: [null, [Validators.required]],
    callname: [],
    birthdate: [],
    nationality: [],
  });

  constructor(
    protected personService: PersonService,
    protected countryService: CountryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ person }) => {
      this.updateForm(person);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const person = this.createFromForm();
    if (person.id !== undefined) {
      this.subscribeToSaveResponse(this.personService.update(person));
    } else {
      this.subscribeToSaveResponse(this.personService.create(person));
    }
  }

  trackCountryById(_index: number, item: ICountry): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPerson>>): void {
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

  protected updateForm(person: IPerson): void {
    this.editForm.patchValue({
      id: person.id,
      name: person.name,
      surname1: person.surname1,
      surname2: person.surname2,
      nickname: person.nickname,
      graphicsName: person.graphicsName,
      callname: person.callname,
      birthdate: person.birthdate,
      nationality: person.nationality,
    });

    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing(
      this.countriesSharedCollection,
      person.nationality
    );
  }

  protected loadRelationshipsOptions(): void {
    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) =>
          this.countryService.addCountryToCollectionIfMissing(countries, this.editForm.get('nationality')!.value)
        )
      )
      .subscribe((countries: ICountry[]) => (this.countriesSharedCollection = countries));
  }

  protected createFromForm(): IPerson {
    return {
      ...new Person(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      surname1: this.editForm.get(['surname1'])!.value,
      surname2: this.editForm.get(['surname2'])!.value,
      nickname: this.editForm.get(['nickname'])!.value,
      graphicsName: this.editForm.get(['graphicsName'])!.value,
      callname: this.editForm.get(['callname'])!.value,
      birthdate: this.editForm.get(['birthdate'])!.value,
      nationality: this.editForm.get(['nationality'])!.value,
    };
  }
}
